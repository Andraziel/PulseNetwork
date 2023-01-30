package fr.isen.pulse.pulsenetwork.classes

import com.google.gson.annotations.SerializedName


data class Post (

  @SerializedName("description" ) var description : String? = null,
  @SerializedName("id"          ) var id          : String? = null,
  @SerializedName("image"       ) var image       : String? = null,
  @SerializedName("titre"       ) var titre       : String? = null,
  @SerializedName("like"       ) var like       : String? = null,
  @SerializedName("dislike"       ) var dislike       : String? = null,
  @SerializedName("auteur"       ) var auteur       : String? = null

)