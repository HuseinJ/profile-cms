package com.hjusic.api.profileapi.pageComponent.model

interface PageComponents {
    fun trigger(pageComponentEvent: PageComponentEvent): PageComponent;
}