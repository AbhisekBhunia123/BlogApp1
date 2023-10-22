package com.restapi.controllers;

import java.util.List;
import java.util.Set;

import com.blogapp.entities.Post;
import com.blogapp.entities.User;

public class CustomResponse {
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private List<Post> posts;
    private Set<String> tags;
    private Set<String> authors;
    private User user;
    private boolean sortPaging;
    private String sortType;
    private boolean filterPage;
    private List<String> filterTags;
    private List<String> filterAuthor;
    private String startDate;
    private String endDate;
    private String searchText;
    
    

	public boolean isFilterPage() {
		return filterPage;
	}

	public void setFilterPage(boolean filterPage) {
		this.filterPage = filterPage;
	}

	public List<String> getFilterTags() {
		return filterTags;
	}

	public void setFilterTags(List<String> filterTags) {
		this.filterTags = filterTags;
	}

	public List<String> getFilterAuthor() {
		return filterAuthor;
	}

	public void setFilterAuthor(List<String> filterAuthor) {
		this.filterAuthor = filterAuthor;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public boolean isSortPaging() {
		return sortPaging;
	}

	public void setSortPaging(boolean sortPaging) {
		this.sortPaging = sortPaging;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CustomResponse(int currentPage, int totalPages, long totalItems, List<Post> posts, Set<String> tags, Set<String> authors) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.posts = posts;
        this.tags = tags;
        this.authors = authors;
    }

    // Getters and setters
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }
}
