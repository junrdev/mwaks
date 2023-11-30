package ke.ac.mwaks.util

data class ResponseWrapper <T, Boolean>(var data: T?, var isCompleted: kotlin.Boolean ){

    init {
        data=null
        isCompleted = false
    }

}