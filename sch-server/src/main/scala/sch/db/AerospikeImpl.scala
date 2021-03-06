package sch.db

import com.aerospike.client.{AerospikeClient, Bin, Key, Value}
import sch.domain.Class.ClassResponse
import sch.domain.Course.Course
import sch.domain.Group.Group
import sch.domain.Room.Room
import sch.domain.Teacher.Teacher
import sch.domain.{Class, Schedule}
import sch.services.ScheduleTrait
import sch.utils.{Constants, Seeder}

import scala.collection.mutable.ListBuffer

class AerospikeImpl extends ScheduleTrait {

  private val interface = "localhost"
  private val port = 3000

  val client = new AerospikeClient(interface, port)

  var availableClasses: List[sch.domain.Class.Class] = List()
  var generatedSchedules: ListBuffer[Schedule] = ListBuffer()
  var availableRooms: List[Room] = List()
  var availableCourses: List[Course] = List()
  var availableTeachers: List[Teacher] = List()
  var availableGroups: List[Group] = List()
  var optimizedSchedule: Schedule = new Schedule(this)

  override def getOptimizedSchedule: Schedule = {
    var schedulesGenomes = generatedSchedules.map(schedule => schedule -> schedule.calculateFitnessRate())
    schedulesGenomes = schedulesGenomes.sortWith(_._2 < _._2)

    for (i <- 1 to 100) {
      val firstGenome = schedulesGenomes(0)._1
      val secondGenome = schedulesGenomes(1)._1
      val thirdGenome = schedulesGenomes(3)._1
      val firstNewGenome = firstGenome.mutate(secondGenome)
      val secondNewGenome = secondGenome.mutate(thirdGenome)

      schedulesGenomes += (firstNewGenome -> firstNewGenome.calculateFitnessRate())
      schedulesGenomes += (secondNewGenome -> secondNewGenome.calculateFitnessRate())
      schedulesGenomes = schedulesGenomes.sortWith(_._2 < _._2)
      schedulesGenomes = schedulesGenomes.takeRight(4)
    }
    schedulesGenomes = schedulesGenomes.sortWith(_._2 > _._2)
    println("Most optimized " + schedulesGenomes.head._2)
    optimizedSchedule = schedulesGenomes.head._1
    schedulesGenomes.head._1
  }

  override def addTeacher(teacher: Teacher): Unit = {

    val key = new Key("test", "teacher", teacher.id)
    val name = new Bin("name", Value.get(teacher.name))
    val id = new Bin("id", Value.get(teacher.id))

    client.put(null, key, name, id)
  }

  override def addClass(schClass: Class.Class): Unit = {

    println("Class added")
  }

  override def addCourse(course: Course): Unit = {

    println("Course added")
  }

  override def getClassesByDay(weekDay: Int): List[sch.domain.Class.ClassResponse] = {

    optimizedSchedule.timeSlots
      .zipWithIndex
      .filter(x => isPreferedDay(x._2, weekDay)).flatMap(x => {
      x._1.map(z => ClassResponse(
                            z.teacherId,
                            z.courseId,
                            z.hours,
                            z.groupId,
                            z.isEquiped,
                            ((x._2 % (Constants.AMOUNT_OF_HOURS * availableRooms.size)) / 12).toString,
                            (x._2 % 11).toString))
    }).toList
  }

  override def getAllClasses: List[sch.domain.Class.ClassResponse] = {

    generatedSchedules.head.timeSlots.filter(x => x.nonEmpty).flatten.toList
  }

  private def isPreferedDay(currentDay: Int, desiredDay: Int): Boolean = {

    (currentDay % (Constants.NUMBER_OF_ROOMS * Constants.AMOUNT_OF_HOURS)) % Constants.WORK_DAYS == (desiredDay)
  }
  
  def seedData(): Unit = {

    availableClasses = Seeder.readClasses()
    availableCourses = Seeder.readCourses()
    availableGroups = Seeder.readStudents()
    availableRooms = Seeder.readRooms()
    availableTeachers = Seeder.readTeachers()
  }

  def generateSchedules(): Unit = {

    for (i <- 0 until 5) {
      val schedule = new Schedule(this)
      availableClasses.foreach(schedule.addClass)
      generatedSchedules += schedule
    }
  }

  override def getTeacherById(teacherId: String): Teacher = {

    availableTeachers.find(x => x.id == teacherId).head
  }

  override def getGroupById(groupId: String): Group = {

    availableGroups.find(x => x.id == groupId).head
  }

  override def getRoomById(roomId: String): Room = {

    availableRooms.find(x => x.id == roomId).head
  }

  override def getCourseById(courseId: String): Course = {

    availableCourses.find(x => x.courseId == courseId).head
  }

  override def getAllTeachers: List[Teacher] = availableTeachers

  override def getAllGroups: List[Group] = availableGroups

  override def getAllRooms: List[Room] = availableRooms

  override def getAllCourses: List[Course] = availableCourses
}
