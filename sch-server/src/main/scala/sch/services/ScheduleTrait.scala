package sch.services

import sch.domain.Course.Course
import sch.domain.Teacher.Teacher

trait ScheduleTrait {

  def addTeacher(teacher: Teacher): Unit
  def addClass(schClass: sch.domain.Class.Class): Unit
  def addCourse(course: Course): Unit
  def getClassesByDay(weekDay: Int): List[sch.domain.Class.Class]
  def getAllClasses(): List[sch.domain.Class.Class]
}
