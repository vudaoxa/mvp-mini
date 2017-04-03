package net.mfilm.ui.videos.model

import android.net.Uri

import java.io.Serializable

/**
 * Created by Dieu on 14/02/2017.
 */

class MPlayer(val builder: MPlayer.Builder) : Serializable {

    val name: String?
        get() = builder.name

    val uri: Uri
        get() = Uri.parse(builder.uri)

    val uris: ArrayList<Uri>?
        get() = builder.getUris()

    val isPrerExtensisonDecodes: Boolean
        get() = builder.isPrerExtensisonDecodes

    val drmSchemeUuiddExtra: String?
        get() = builder.drmSchemeUuiddExtra
    val drmLicenseUrl: String?
        get() = builder.drmLicenseUrl

    val keyRequestPropertiesArray: Array<String>?
        get() = builder.keyRequestPropertiesArray

    val action: String?
        get() = builder.action

    val extensionExtra: String?
        get() = builder.extensionExtra
    val uriListExtra: Array<String>?
        get() = builder.uriListExtra

    class Builder : Serializable {
        var name: String? = null
        var uri: String? = null
        var uriss: ArrayList<Uri>? = null
        var isPrerExtensisonDecodes = false
        var drmSchemeUuiddExtra: String? = null
        var drmLicenseUrl: String? = null
        var keyRequestPropertiesArray: Array<String>? = null
        var action: String? = null
        var extensionExtra: String? = null
        var uriListExtra: Array<String>? = null

        fun setName(name: String): Builder {
            this.name = name
            return this
        }

        fun setUri(uri: String): Builder {
            this.uri = uri
            return this
        }

        fun setUris(uris: ArrayList<Uri>): Builder {
            this.uriss = uris
            return this
        }

        fun setPrerExtensisonDecodes(prerExtensisonDecodes: Boolean): Builder {
            isPrerExtensisonDecodes = prerExtensisonDecodes
            return this
        }

        fun setDrmSchemeUuiddExtra(drmSchemeUuiddExtra: String): Builder {
            this.drmSchemeUuiddExtra = drmSchemeUuiddExtra
            return this
        }

        fun setDrmLicenseUrl(drmLicenseUrl: String): Builder {
            this.drmLicenseUrl = drmLicenseUrl
            return this
        }

        fun setKeyRequestPropertiesArray(keyRequestPropertiesArray: Array<String>): Builder {
            this.keyRequestPropertiesArray = keyRequestPropertiesArray
            return this
        }

        fun setAction(action: String): Builder {
            this.action = action
            return this
        }

        fun setExtensionExtra(extensionExtra: String?): Builder {
            this.extensionExtra = extensionExtra
            return this
        }

        fun setUriListExtra(uriListExtra: Array<String>): Builder {
            this.uriListExtra = uriListExtra
            return this
        }

        fun getUris(): ArrayList<Uri>? {
            val urissx: ArrayList<Uri>? = null
            if (uriss != null) {
                uriss = arrayListOf<Uri>()
                for (item in uriss!!) {
                    urissx?.add(item)
                }
            }
            return urissx
        }

        fun buidler(): MPlayer {
            return MPlayer(this)
        }
    }
}
