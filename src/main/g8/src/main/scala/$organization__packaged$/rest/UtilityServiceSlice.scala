package com.microworkflow.rest

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

class UtilityServiceSlice {

  val routes: Route = {
    path("") {
      pathEnd {
        complete("here")
      }
    } ~ path("info") {
      get {
        complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, buildinfo.BuildInfo.toJson)))
      }
    }
  }
}
