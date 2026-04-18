package com.smarttour.explore.api.model

data class ProfileResponse(
    val fullName: String,
    val initials: String,
    val levelLabel: String,
    val stats: List<ProfileStatResponse>,
    val settings: List<ProfileSettingResponse>
)

data class ProfileStatResponse(
    val value: String,
    val label: String
)

data class ProfileSettingResponse(
    val label: String,
    val value: String
)
