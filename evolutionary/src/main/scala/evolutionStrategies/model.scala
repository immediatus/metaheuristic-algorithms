package ua.org.scala
package evolutionStrategies

object model {

  case class City(name : String, latitude : Double, longtitude : Double)

  def getCities(count : Int): List[City] = {
    import collection.mutable.Buffer, util.Random._
    assert(count <= europe.length)

    def get0(i : Int, cities : Buffer[City]): List[City] =
      if(i == 0) Nil
      else {
        val elem = cities(nextInt(cities.length))
        elem :: get0(i - 1, cities - elem)
      }

    get0(count, europe.toBuffer)
  }

  val europe = List(
    City("Brussels"   ,50797.140  ,4361.572   ),
    City("Dublin"     ,53309.435  ,-6284.180  ),
    City("London"     ,51465.872  ,-131.836   ),
    City("Paris"      ,48797.698  ,2351.074   ),
    City("Reykjavik"  ,64133.219  ,-21886.139 ),
    City("Luxembourg" ,49537.568  ,6130.371   ),
    City("Amsterdam"  ,52320.120  ,4888.916   ),
    City("Berlin"     ,52480.996  ,13414.307  ),
    City("Copenhagen" ,55620.139  ,12579.346  ),
    City("Oslo"       ,59876.442  ,10766.602  ),
    City("Stockholm"  ,59292.446  ,18061.523  ),
    City("Helsinki"   ,60134.576  ,24949.951  ),
    City("Tallinn"    ,59363.808  ,24763.184  ),
    City("Riga"       ,56845.768  ,24082.031  ),
    City("Vilnius"    ,54581.401  ,25268.555  ),
    City("Minsk"      ,53810.166  ,27553.711  ),
    City("Warsaw"     ,52129.891  ,21005.859  ),
    City("Moscow"     ,55661.888  ,37617.188  ),
    City("Kiev"       ,50355.742  ,30541.992  ),
    City("Chisinau"   ,46916.253  ,28828.125  ),
    City("Bucharest"  ,44319.656  ,26059.570  ),
    City("Sofia"      ,42581.130  ,23312.988  ),
    City("Ankara"     ,39943.436  ,32857.132  ),
    City("Athens"     ,37852.881  ,23730.469  ),
    City("Nicosia"    ,35099.537  ,33365.479  ),
    City("Tirana"     ,41283.861  ,19808.350  ),
    City("Skopje"     ,41949.141  ,21456.299  ),
    City("Podgorica"  ,42380.730  ,19281.006  ),
    City("Belgrade"   ,44752.455  ,20456.543  ),
    City("Sarajevo"   ,43784.843  ,18347.168  ),
    City("Zagreb"     ,45757.815  ,15974.121  ),
    City("Ljubljana"  ,45994.926  ,14490.967  ),
    City("Rome"       ,41842.830  ,12491.455  ),
    City("Madrid"     ,40369.427  ,-3691.406  ),
    City("Lisbon"     ,38648.910  ,-9140.625  ),
    City("Bern"       ,46895.737  ,7437.744   ),
    City("Vienna"     ,48142.143  ,16380.615  ),
    City("Prague"     ,50066.778  ,14419.556  ),
    City("Bratislava" ,48098.138  ,17105.713  ),
    City("Budapest"   ,47440.969  ,19039.307  )
  )
}

