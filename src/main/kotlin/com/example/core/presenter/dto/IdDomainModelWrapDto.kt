package com.example.core.presenter.dto

data class IdDomainModelWrapDto<IdT, DomainT>(
    val id: IdT,
    val domainModel: DomainT
)
