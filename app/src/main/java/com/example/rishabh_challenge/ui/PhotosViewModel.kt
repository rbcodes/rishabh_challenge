package com.example.android.searchdebounce.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rishabh_challenge.db.TrekkingRepository
import com.example.rishabh_challenge.db.Photos

class PhotosViewModel(repository: TrekkingRepository): ViewModel()
 {
     val photos: LiveData<List<Photos>> = repository.getAllPhotos("1")

     class TrekkingViewModelFactory(private val repository: TrekkingRepository)
         : ViewModelProvider.Factory {

         override fun <T : ViewModel> create(modelClass: Class<T>): T {

             if (modelClass.isAssignableFrom(PhotosViewModel::class.java)) {
                 return PhotosViewModel(repository) as T
             }
             throw IllegalArgumentException("Unknown ViewModel class")
         }
     }
}

