import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import sch.db.AerospikeImpl
import sch.routes.HttpRoutes

object SchBoot extends App {

  implicit val system: ActorSystem = ActorSystem("root")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val dbProvider = new AerospikeImpl()
  val httpRoutes = new HttpRoutes(dbProvider)

  dbProvider.readClasses()
  dbProvider.generateSchedules()
  private val interface = "172.20.20.195"
  private val port = 8080
  system.log.info("Server started at {}:{}", interface, port)
  Http().bindAndHandle(httpRoutes.routes, interface, port)
}