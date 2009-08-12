package com.liftcode.model

/*
 * Copyright 2006-2009 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */

import _root_.java.sql.{ResultSet, Types}
import _root_.java.util.Date
import _root_.java.lang.reflect.Method

import _root_.net.liftweb._
import util._
import Helpers._
import http._
import S._
import js._

import _root_.scala.xml.{NodeSeq}
import _root_.net.liftweb.mapper.{DriverType, Mapper, MappedDateTime}

class MappedTimestamp[T<:Mapper[T]](val owner: T) extends MappedDateTime[T](owner) {

  override def toLong: Long = is match {
    case null => 0L
    case d: Date => d.getTime
  }

  /**
   * Get the JDBC SQL Type for this field
   */
  override def targetSQLType = Types.BIGINT 

  /**
   * Create an input field for the item
   */
  override def _toForm: Box[NodeSeq] =
  S.fmapFunc({
    s: List[String] => this.setFromAny(s)
  }){
    funcName => Full(
      <input type='text' id={fieldId}
      name={funcName} lift:gc={funcName}
      value={is match {case null => "" case s => s.toString}}/>
    )
  }

  override def jdbcFriendly(field : String) : Object = is match {
    case null => null
    case d => new _root_.java.lang.Long(d.getTime)
  }

  override def real_convertToJDBCFriendly(value: Date): Object = if (value == null) null else new _root_.java.lang.Long(value.getTime)

  /**
   * Given the driver type, return the string required to create the column in the database
   */
  override def fieldCreatorString(dbType: DriverType, colName: String): String = colName + " " + dbType.longColumnType
}
