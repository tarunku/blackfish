package iris_spring

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @author ${user.name}
 */

@SpringBootApplication
class IrisApplication

object App {
  
  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)
  
  def main(args : Array[String]) {
    println( "Hello World {Tarun}!" )
    
    SpringApplication.run(classOf[IrisApplication]);
    
    //println("concat arguments = " + foo(args))
  }

}
