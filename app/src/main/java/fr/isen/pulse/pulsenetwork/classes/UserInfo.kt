package fr.isen.pulse.pulsenetwork.classes

import com.google.gson.annotations.SerializedName;

public class UserInfo (
    @SerializedName("idUser") var idUser  : String? = null,
    @SerializedName("firstName") var firstName : String? = null,
    @SerializedName("lastName") var lastName          : String? = null,
    @SerializedName("schoolName") var schoolName          : String? = null,
    @SerializedName("image") var image          : String? = null


):java.io.Serializable
