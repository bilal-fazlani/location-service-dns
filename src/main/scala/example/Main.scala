package example

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.github.mkroli.dns4s.akka._

import scala.concurrent.duration.DurationDouble
import scala.language.postfixOps

object Main extends App {
  println(s"starting dns server...")

  implicit val system: ActorSystem = ActorSystem("DnsServer")
  implicit val timeout: Timeout = Timeout(5 seconds)

  IO(Dns) ? Dns.Bind(system.actorOf(Props[DnsActor]), 53)
}
