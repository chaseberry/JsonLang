package edu.csh.chase.jsonlang.engine.parsing

import edu.csh.chase.jsonlang.engine.NativeFunction
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.*

object CoreLoader {

    val corePackage = "edu.csh.chase.jsonlang.engine.Core"

    fun loadCoreFunctions(): List<Class<NativeFunction>> {
        val list = ArrayList<Class<NativeFunction>>()
        val urls = Thread.currentThread().contextClassLoader.getResources(corePackage.replace(".", File.separator))

        val names = ArrayList<String>()
        val loader = URLClassLoader(File(urls.nextElement().file).listFiles().map {
            names.add(it.nameWithoutExtension)
            URL("file://${it.absolutePath}")
        }.toTypedArray())

        names.forEach {
            try {
                list.add(loader.loadClass("$corePackage.$it") as Class<NativeFunction>)
            } catch(e: Exception) {
                throw RuntimeException("Failed to load CoreFunction $it")
            }
        }

        return list
    }


}