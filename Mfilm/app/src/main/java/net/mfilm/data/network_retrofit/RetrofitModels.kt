package net.mfilm.data.network_retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import timber.log.Timber
import java.io.Serializable

/**
 * Created by tusi on 5/14/17.
 */

open class MResponse(
        @SerializedName("status")
        @Expose
        var status: Boolean? = null,
        @SerializedName("message")
        @Expose
        var message: String? = null) : Serializable

class Category : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("desciption")
    @Expose
    var desciption: String? = null
    @SerializedName("pivot")
    @Expose
    var pivot: Pivot? = null
}

class CategoriesResponse : MResponse() {
    @SerializedName("data")
    @Expose
    var data: List<Category>? = null
}

open class MPaging : Serializable {
    @SerializedName("total")
    @Expose
    var total: Int? = null
    @SerializedName("per_page")
    @Expose
    var perPage: String? = null
    @SerializedName("current_page")
    @Expose
    var currentPage: Int? = null
    @SerializedName("last_page")
    @Expose
    var lastPage: Int? = null
    @SerializedName("next_page_url")
    @Expose
    var nextPageUrl: String? = null
    @SerializedName("prev_page_url")
    @Expose
    var prevPageUrl: String? = null
    @SerializedName("from")
    @Expose
    var from: Int? = null
    @SerializedName("to")
    @Expose
    var to: Int? = null
}

class MangasPaging : MPaging() {
    @SerializedName("data")
    @Expose
    var mangas: List<Manga>? = null
}

class MangaDetailResponse : MResponse() {
    @SerializedName("data")
    @Expose
    var manga: Manga? = null
}

class Manga : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("source")
    @Expose
    var source: Int? = null
    @SerializedName("other_name")
    @Expose
    var otherName: String? = null
    @SerializedName("translator")
    @Expose
    var translator: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("author")
    @Expose
    var author: String? = null
    @SerializedName("cover_url")
    @Expose
    var coverUrl: String? = null
    @SerializedName("cover_file")
    @Expose
    var coverFile: String? = null
    @SerializedName("summary")
    @Expose
    var summary: String? = null
    @SerializedName("note")
    @Expose
    var note: String? = null
    @SerializedName("views")
    @Expose
    var views: Int? = null
    @SerializedName("updated_time")
    @Expose
    var updatedTime: Long? = null
    @SerializedName("cover")
    @Expose
    var cover: String? = null
    @SerializedName("total_chap")
    @Expose
    var totalChap: Int? = null
    @SerializedName("category")
    @Expose
    var categories: List<Category>? = null

    private var longClicked = false
    fun onLongClicked(longClicked: Boolean, fLongClicked: (() -> Unit)? = null) {
        this.longClicked = longClicked
        if (longClicked) {
            fLongClicked?.invoke()
        }
    }

    fun onClicked(f: (() -> Unit)? = null) {
        if (!longClicked) {
            f?.invoke()
        } else {
            longClicked = false
        }
    }
}

class MangasResponse : MResponse() {
    @SerializedName("data")
    @Expose
    var mangasPaging: MangasPaging? = null
}

class Pivot : Serializable {

    @SerializedName("manga_id")
    @Expose
    var mangaId: Int? = null
    @SerializedName("category_id")
    @Expose
    var categoryId: Int? = null
}

class Chapters : MPaging() {
    @SerializedName("data")
    @Expose
    var data: List<Chapter>? = null
}

class Chapter : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("release_time")
    @Expose
    var releaseTime: Int? = null
    @SerializedName("manga_id")
    @Expose
    var mangaId: Int? = null
    override fun toString(): String {
        return "$name --- $id"
    }
}

class ChaptersResponse : MResponse() {
    @SerializedName("data")
    @Expose
    var chapters: Chapters? = null
}

class ChapterDetail : Serializable {
    @SerializedName("manga")
    @Expose
    var manga: Manga? = null
    @SerializedName("chapter")
    @Expose
    var chapter: Chapter? = null
    @SerializedName("next_chapter")
    @Expose
    var nextChapter: Chapter? = null
    @SerializedName("previous_chapter")
    @Expose
    var previousChapter: Chapter? = null
}

class ChapterDetailResponse : MResponse() {
    @SerializedName("data")
    @Expose
    var data: ChapterDetail? = null
}

class ChapterImage : Serializable {
    @SerializedName("url")
    @Expose
    var url: String? = null

    fun isImage(): Boolean {
        val x = url?.run {
            contains("png", true) || contains("jpg", true) || contains("jpeg", true)
        } ?: false
        if (!x) {
            Timber.e("----isImage-------- $url----------------------")
        }
        return x
    }
}

class ChapterImagesResponse : MResponse() {
    @SerializedName("data")
    @Expose
    var data: List<ChapterImage>? = null
}