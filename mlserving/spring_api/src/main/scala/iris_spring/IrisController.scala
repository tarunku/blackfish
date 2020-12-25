package iris_spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation._

import ml.combust.bundle.BundleFile
import ml.combust.mleap.runtime.MleapSupport._
import resource._
import ml.combust.mleap.runtime._
import ml.combust.mleap.core.types._
import ml.combust.mleap.runtime.frame.{ DefaultLeapFrame, Row }

@RestController
class IrisController {

  def predict(
    model:       String,
    SepalLength: Double,
    SepalWidth:  Double,
    PetalLength: Double,
    PetalWidth:  Double,
    Species:     String = "Setosa"): Double = {

    val zipBundleM = (for (bundle <- managed(BundleFile(model))) yield {
      bundle.loadMleapBundle().get
    }).opt.get

    val mleapPipeline = zipBundleM.root

    println(mleapPipeline.model.toString())

    // Create MLeap Frame
    val schema: StructType = StructType(
      StructField("SepalLength", ScalarType.Double),
      StructField("SepalWidth", ScalarType.Double),
      StructField("PetalLength", ScalarType.Double),
      StructField("PetalWidth", ScalarType.Double),
      StructField("Species", ScalarType.String)).get

    val dataset = Seq(
      Row(SepalLength, SepalWidth, PetalLength, PetalWidth, Species))

    val leapFrame = DefaultLeapFrame(schema, dataset)

    println(leapFrame.toString())

    // Predict Iris data points.
    val prediction = mleapPipeline.transform(leapFrame)

    //println(prediction.get.select("prediction").get.dataset.map(_.getDouble(0)).head)
    prediction.get.select("prediction").get.dataset.map(_.getDouble(0)).head
  }

  @RequestMapping(value = Array("/predict-test/win"), method = Array(RequestMethod.GET))
  @ResponseStatus(HttpStatus.OK)
  def predict_win(
    @RequestParam SepalLength: Double,
    @RequestParam SepalWidth:  Double,
    @RequestParam PetalLength: Double,
    @RequestParam PetalWidth:  Double): Double = {

    val model = "jar:file:/__Code/mdl_lfy/model/iris_spark_model.zip"

    //predict(model, 7.1d, 3d, 5.9d, 2.1d)
    predict(model, SepalLength, SepalWidth, PetalLength, PetalWidth)
  }

   
  @RequestMapping(value = Array("/predict-test/contr"), method = Array(RequestMethod.GET))
  @ResponseStatus(HttpStatus.OK)
  def predict_container(
    @RequestParam SepalLength: Double,
    @RequestParam SepalWidth:  Double,
    @RequestParam PetalLength: Double,
    @RequestParam PetalWidth:  Double): Double = {

    val model = "jar:file:/model/iris_spark_model.zip"

    predict(model, SepalLength, SepalWidth, PetalLength, PetalWidth)
  }
}