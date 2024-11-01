import android.content.Context
import java.io.IOException
import java.util.Properties

object Config {
    private const val CONFIG_FILE = "ApiConfig.properties"
    private var properties: Properties? = null

    fun init(context: Context) {
        properties = Properties()
        try {
            context.assets.open(CONFIG_FILE).use { inputStream ->
                properties?.load(inputStream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getBaseUrl(): String {
        return properties?.getProperty("base_url") ?: ""
    }

    fun getApiKey(): String {
        return properties?.getProperty("api_key") ?: ""
    }
}