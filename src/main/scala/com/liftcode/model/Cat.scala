/*
 * Cat.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.liftcode.model

import net.liftweb._
import mapper._
import util._
import Helpers._  

/**
 * An O-R mapped "Cat" class that uses a MAC address as the primary key
 */
class Cat extends KeyedMapper[String, Cat] { 
 def getSingleton = Cat
 /* MAC address as primary key */
 def primaryKeyField = mac
 object mac extends MappedStringIndex(this, 17) with IndexedField[String] {
	override def writePermission_? = true
	override def dbDisplay_? = true
	override def dbAutogenerated_? = false
	
	override lazy val defaultValue = randomString(maxLen)
	
	private var myDirty = false
	override def dirty_? = myDirty
	override def dirty_?(b : Boolean) = { myDirty = b; super.dirty_?(b) }
	override def fieldCreatorString(dbType: DriverType, colName: String): String = colName+" CHAR("+maxLen+") NOT NULL "
 }
 object name extends MappedPoliteString(this, 128)
 object weight extends MappedInt(this)
 object ts extends MappedTimestamp(this)
}

object Cat extends Cat with KeyedMetaMapper[String, Cat] {
 override def dbTableName = "cats" // define the DB table name
// override def dbIndexes = UniqueIndex(mac) :: Nil
}
