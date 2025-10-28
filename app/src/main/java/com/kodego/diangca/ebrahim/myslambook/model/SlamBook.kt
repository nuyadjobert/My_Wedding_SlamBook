package com.kodego.diangca.ebrahim.myslambook.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.kodego.diangca.ebrahim.myslambook.R

data class SlamBook(
    val profilePic: Int = R.drawable.profile_icon
) : Parcelable {

    // Personal Information
    var firstName: String = ""
    var lastName: String = ""
    var nickName: String = ""
    var howIKnowTheCouple: String = "" // Relationship to the couple

    var birthMonth:String=""
    var birthDay:String=""
    var birthYear:String=""
    var birthDate:String=""

    var guestType:String=""
    var email: String = ""
    var contactNo: String = ""
    var address: String = ""

    // Wedding-specific fields
    var whatLoveMeansToThem: String = "" // What I admire about their love
    var wishesForTheirMarriage: String = "" // My wishes for their marriage
    var favoriteMemory: String = "" // My favorite memory with the couple
    var favoriteThingAboutCouple: String = "" // My favorite thing about the couple
    var marriageAdvice: String = "" // My advice for the newlyweds
    var coupleRating: Int = 5 // Rate the couple

    constructor(parcel: Parcel) : this(parcel.readInt()) {
        firstName = parcel.readString() ?: ""
        lastName = parcel.readString() ?: ""
        nickName = parcel.readString() ?: ""
        howIKnowTheCouple = parcel.readString() ?: ""

        // Read birthdate components
        birthMonth = parcel.readString() ?: ""
        birthDay = parcel.readString() ?: ""
        birthYear = parcel.readString() ?: ""

        guestType=parcel.readString()?:""
        email = parcel.readString() ?: ""
        contactNo = parcel.readString() ?: ""
        address = parcel.readString() ?: ""
        whatLoveMeansToThem = parcel.readString() ?: ""
        wishesForTheirMarriage = parcel.readString() ?: ""
        favoriteMemory = parcel.readString() ?: ""
        favoriteThingAboutCouple = parcel.readString() ?: ""
        marriageAdvice = parcel.readString() ?: ""
        coupleRating = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(profilePic)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(nickName)
        parcel.writeString(howIKnowTheCouple)

        // Write birthdate components
        parcel.writeString(birthMonth)
        parcel.writeString(birthDay)
        parcel.writeString(birthYear)

        parcel.writeString(guestType)
        parcel.writeString(email)
        parcel.writeString(contactNo)
        parcel.writeString(address)
        parcel.writeString(whatLoveMeansToThem)
        parcel.writeString(wishesForTheirMarriage)
        parcel.writeString(favoriteMemory)
        parcel.writeString(favoriteThingAboutCouple)
        parcel.writeString(marriageAdvice)
        parcel.writeInt(coupleRating)
    }

    override fun describeContents(): Int {
        return 0
    }

    // Validation methods
    fun hasBasicInfo(): Boolean {
        return firstName.isNotBlank() && lastName.isNotBlank() && howIKnowTheCouple.isNotBlank()
    }

    fun isValid(): Boolean {
        return hasBasicInfo() && coupleRating in 1..5
    }

    fun getFullName(): String {
        return "$firstName $lastName".trim()
    }

    fun printLog() {
        Log.d("WEDDING_SLAM_BOOK", toString())
    }

    override fun toString(): String {
        return """
            WeddingSlamBook(
                profilePic=$profilePic, 
                fullName='${getFullName()}', 
                nickName='$nickName', 
                birthdate='${birthMonth} ${birthDay}, ${birthYear}',
                relationshipToCouple='$howIKnowTheCouple', 
                guestType='$guestType
                email='$email', 
                contactNo='$contactNo', 
                address='$address', 
                coupleRating=$coupleRating
            )
        """.trimIndent()
    }

    companion object CREATOR : Parcelable.Creator<SlamBook> {
        override fun createFromParcel(parcel: Parcel): SlamBook {
            return SlamBook(parcel)
        }

        override fun newArray(size: Int): Array<SlamBook?> {
            return arrayOfNulls(size)
        }

        // Factory method for creating a default Wedding SlamBook
        fun createDefault(): SlamBook {
            return SlamBook().apply {
                firstName = "Guest"
                howIKnowTheCouple = "Friend"
                coupleRating = 5
            }
        }

        // ⭐ NEW: Factory method to create sample completed data (fixes the error)
        fun createCompletedSample(): SlamBook {
            return SlamBook(R.drawable.profile_icon).apply {
                // --- Page 1 Data ---
                firstName = "Jobert"
                lastName = "Cruz"
                nickName = "Jojo"
                howIKnowTheCouple = "Best Man / Groom's Brother"
                guestType = "Groom Side"
                birthMonth = "August"
                birthDay = "15"
                birthYear = "1990"
                email = "jobert.cruz@example.com"
                contactNo = "0917-123-4567"
                address = "Manila, Philippines"

                // --- Page 2 & 3 Data ---
                whatLoveMeansToThem = "They are inseparable, true soulmates."
                wishesForTheirMarriage = "May your life together be filled with laughter and love."
                favoriteMemory = "The time we all went hiking in Palawan."
                favoriteThingAboutCouple = "Their matching terrible singing voices."
                marriageAdvice = "Always choose kindness, even on hard days."
                coupleRating = 5
            }
        }
    }
}
