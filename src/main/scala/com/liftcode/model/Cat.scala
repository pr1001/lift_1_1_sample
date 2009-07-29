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
 object mac extends MappedStringIndex(this, 17) {
	override def writePermission_? = true
	override def dbDisplay_? = true
	
	override lazy val defaultValue = randomString(maxLen)
	
 	private var myDirty_? = false
 	override protected def dirty_?(b: Boolean) = myDirty_? = b
 	override def dirty_? = myDirty_?
 }
 object name extends MappedPoliteString(this, 128)
 object weight extends MappedInt(this)
}

object Cat extends Cat with KeyedMetaMapper[String, Cat] {
 override def dbTableName = "cats" // define the DB table name
// override def dbIndexes = UniqueIndex(mac) :: Nil
}
