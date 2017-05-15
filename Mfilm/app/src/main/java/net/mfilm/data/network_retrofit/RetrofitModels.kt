package net.mfilm.data.network_retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
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
        var message: Any? = null) : Serializable

class Category : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("desciption")
    @Expose
    var desciption: Any? = null
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
    var prevPageUrl: Any? = null
    @SerializedName("from")
    @Expose
    var from: Int? = null
    @SerializedName("to")
    @Expose
    var to: Int? = null
}

class MangaPaging : MPaging() {
    @SerializedName("data")
    @Expose
    var data: List<Manga>? = null
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
    var otherName: Any? = null
    @SerializedName("translator")
    @Expose
    var translator: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("author")
    @Expose
    var author: Any? = null
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
    var note: Any? = null
    @SerializedName("views")
    @Expose
    var views: Int? = null
    @SerializedName("updated_time")
    @Expose
    var updatedTime: Int? = null
    @SerializedName("cover")
    @Expose
    var cover: String? = null
    @SerializedName("total_chap")
    @Expose
    var totalChap: Int? = null
    @SerializedName("category")
    @Expose
    var category: List<Category>? = null
}

class MangaResponse : MResponse() {
    @SerializedName("data")
    @Expose
    var mangaPaging: MangaPaging? = null
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
    var title: Any? = null
    @SerializedName("release_time")
    @Expose
    var releaseTime: Int? = null
    @SerializedName("manga_id")
    @Expose
    var mangaId: Int? = null

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

class ChapterImg : Serializable {
    @SerializedName("url")
    @Expose
    var url: String? = null
}

class ChapterImgsResponse : MResponse() {
    @SerializedName("data")
    @Expose
    var data: List<ChapterImg>? = null
}