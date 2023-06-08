package com.example.pollista.BusinessModel

data class VoterDetails(
    val optionOneVoted: Boolean?,
    val optionOneVoters: List<String> = listOf(),
    val optionTwoVoters: List<String> = listOf()
)
