package fr.isen.pulse.pulsenetwork.classes

import com.google.gson.annotations.SerializedName

data class Commentaire (

    @SerializedName("auteur"       ) var auteur       : String? = null,
    @SerializedName("commentaire" ) var commentaire : String? = null,
    @SerializedName("id"          ) var id          : String? = null

):java.io.Serializable