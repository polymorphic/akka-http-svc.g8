package com.microworkflow.domain

sealed trait DomainObject

final case class ResponseDocument(body: String) extends DomainObject