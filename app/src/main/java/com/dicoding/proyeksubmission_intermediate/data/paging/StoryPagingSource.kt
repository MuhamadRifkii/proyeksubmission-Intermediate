package com.dicoding.proyeksubmission_intermediate.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.proyeksubmission_intermediate.data.api.ApiService
import com.dicoding.proyeksubmission_intermediate.data.response.ListStoryItem

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        val page = params.key ?: INITIAL_PAGE_INDEX
        return try {
            val response = apiService.getStories(page, params.loadSize)
            val stories = response.listStory
            LoadResult.Page(
                data = stories,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (stories.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}