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

import net.liftweb.mapper.Mapper
import net.liftweb.mapper.MappedDateTime

trait MappedTimestamp[T<:Mapper[T]] extends MappedDateTime[Date] {
  this: T with MappedTimestamp[T] =>

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
  S.fmapFunc({s: List[String] => this.setFromAny(s)}){funcName =>
  Full(<input type='text' id={fieldId}
      name={funcName} lift:gc={funcName}
      value={is match {case null => "" case s => s}}/>)
  }

  override def jdbcFriendly(field : String) : Object = is match {
    case null => null
    case d => new _root_.java.sql.Date(d.getTime)
  }

  override def real_convertToJDBCFriendly(value: Date): Object = if (value == null) null else new _root_.java.lang.Long(value.getTime)

  override def buildSetActualValue(accessor: Method, v: AnyRef, columnName: String): (T, AnyRef) => Unit =
  (inst, v) => doField(inst, accessor, {case f: MappedTimestamp[T] => f.st(toDate(v))})

  override def buildSetLongValue(accessor: Method, columnName: String): (T, Long, Boolean) => Unit =
  (inst, v, isNull) => doField(inst, accessor, {case f: MappedTimestamp[T] => f.st(if (isNull) Empty else Full(new Date(v)))})

  override def buildSetStringValue(accessor: Method, columnName: String): (T, String) => Unit =
  (inst, v) => doField(inst, accessor, {case f: MappedTimestamp[T] => f.st(toDate(v))})

  override def buildSetDateValue(accessor: Method, columnName: String): (T, Date) => Unit =
  (inst, v) => doField(inst, accessor, {case f: MappedTimestamp[T] => f.st(Full(v))})

  override def buildSetBooleanValue(accessor: Method, columnName: String): (T, Boolean, Boolean) => Unit =
  (inst, v, isNull) => doField(inst, accessor, {case f: MappedTimestamp[T] => f.st(Empty)})

  /**
   * Given the driver type, return the string required to create the column in the database
   */
  override def fieldCreatorString(dbType: DriverType, colName: String): String = colName + " " + dbType.longColumnType
}
