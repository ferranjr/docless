package swag

import scala.util.matching.Regex
import enumeratum._

object OperationParameter {
  sealed trait In extends EnumEntry with EnumEntry.Lowercase

  object In extends Enum[In] with CirceEnum[In] {
    case object Path extends In
    case object Query extends In
    case object Header extends In
    case object Form extends In

    override def values = findValues
  }
}

trait OperationParameter {
  def name: String
  def required: Boolean
  def description: Option[String]
  def mandatory: OperationParameter
  protected def as[T <: Type](t: T): OperationParameter
  def asInteger = as(Type.Integer)
  def asNumber = as(Type.Number)
  def asFile = as(Type.File)
  def asBoolean = as(Type.Boolean)
}

case class BodyParameter(description: Option[String] = None,
                         required: Boolean = false,
                         name: String = "body",
                         schema: Option[JsonSchema.Ref] = None) extends OperationParameter {
  override def mandatory = copy(required = true)
  override def as[T <: Type](t: T) = this
}

case class ArrayParameter(name: String,
                          required: Boolean = false,
                          in: OperationParameter.In,
                          description: Option[String] = None,
                          itemType: Type = Type.String,
                          collectionFormat: Option[CollectionFormat] = None,
                          minMax: Option[Range] = None,
                          format: Option[Format] = None) extends OperationParameter {

  def as[T <: Type](t: T) = copy(`itemType` = t)

  override def mandatory = copy(required = true)
}

case class Parameter(name: String,
                     required: Boolean = false,
                     in: OperationParameter.In,
                     description: Option[String] = None,
                     `type`: Type = Type.String,
                     format: Option[Format] = None) extends OperationParameter {

  def as[T <: Type](t: T) = copy(`type` = t)
  override def mandatory = copy(required = true)
}

object Parameter {
  def query(name: String,
            required: Boolean = false,
            description: Option[String] = None,
            `type`: Type = Type.String,
            format: Option[Format] = None) =
    apply(name, required = false, OperationParameter.In.Query, description, `type`, format)

  def path(name: String,
           description: Option[String] = None,
           `type`: Type = Type.String,
           format: Option[Format] = None,
           default: Option[String] = None ) =
    apply(name, required = true, OperationParameter.In.Path, description, `type`, format)
}
