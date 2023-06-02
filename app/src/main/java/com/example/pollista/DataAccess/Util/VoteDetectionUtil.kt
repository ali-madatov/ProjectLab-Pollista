package com.example.pollista.DataAccess.Util

import com.example.pollista.BusinessModel.VoterDetails

object VoteDetectionUtil {

    /**
     * Return true if user voted for the first option, false if for the second option, otherwise null
     */
    fun detectUserVote(userID: String, optionOneVoters: List<String>, optionTwoVoters: List<String>): Boolean? {
        if(optionOneVoters.contains(userID)){
            return true
        }
        else if (optionTwoVoters.contains(userID)){
            return false
        }
        return null
    }

    fun getVoterDetails(userID: String, optionOneVoters: List<String>, optionTwoVoters: List<String>): VoterDetails{
        return VoterDetails(
            detectUserVote(userID, optionOneVoters, optionTwoVoters),
            optionOneVoters,
            optionTwoVoters
        )
    }
}