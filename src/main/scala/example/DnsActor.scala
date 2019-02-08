package example

import java.net.InetSocketAddress

import akka.actor.Actor
import akka.io.IO
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.github.mkroli.dns4s.Message
import com.github.mkroli.dns4s.akka.Dns.Bound
import com.github.mkroli.dns4s.akka._
import com.github.mkroli.dns4s.dsl._

import scala.concurrent.Future
import scala.concurrent.duration.DurationLong
import scala.language.postfixOps


class DnsActor extends Actor {

  val destinationDns = new InetSocketAddress("8.8.8.8", 53)

  import context._
  implicit val timeout: Timeout = Timeout(5 seconds)

  val names:Map[String,String] = Map(
      "bilal.com" -> "127.0.0.1"
    )

    def forwardMessage(message: Message): Future[Message] = (IO(Dns) ? Dns.DnsPacket(message, destinationDns)).mapTo[Message]

    override def receive: PartialFunction[Any, Unit] = {
      case Bound => println("started")
      case Query(q) ~ Questions(QName(host) ~ TypeA() :: Nil) if names.contains(host) => sender ! Response(q) ~ Answers(RRName(host) ~ ARecord(names(host)))
      case message: Message => forwardMessage(message).pipeTo(sender)
    }
  }