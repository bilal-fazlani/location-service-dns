package example

import com.github.mkroli.dns4s.dsl._
import com.github.mkroli.dns4s.akka._
import akka.actor.{Actor, ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object DnsServer extends App {

  class DnsHandlerActor extends Actor {

    val names:Map[String,String] = Map(
      "abc.com" -> "127.0.0.1",
      "pqr.com" -> "127.0.0.1",
      "xyz.com" -> "127.0.0.1",
      "asd.com" -> "127.0.0.1",
    )

    override def receive: PartialFunction[Any, Unit] = {
      case Query(q) ~ Questions(QName(host) ~ TypeA() :: Nil) if names.contains(host) =>
        println(s"query received for host: $host")
        sender ! Response(q) ~ Answers(RRName(host) ~ ARecord(names(host)))
        println(s"query for $host resolved to ${names(host)}")
      case Query(q) ~ Questions(QName(host) ~ TypeA() :: Nil) if !names.contains(host) =>
        println(s"query received for host: $host")
        sender ! Response(q) ~ Answers(RRName(host))
        println(s"query for $host could not be resolved locally")
    }
  }

  implicit val system: ActorSystem = ActorSystem("DnsServer")
  implicit val timeout: Timeout = Timeout(5 seconds)
  private val port = 53

  IO(Dns) ? Dns.Bind(system.actorOf(Props[DnsHandlerActor]), port)
  println(s"DNS server started on port $port")
}
