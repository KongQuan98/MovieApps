import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.movieapps.R
import com.example.movieapps.domain.datamodel.MovieListModel
import com.example.movieapps.ui.theme.Purple40
import com.example.movieapps.viewmodel.AuthState
import com.example.movieapps.viewmodel.LoginAndSignUpViewModel
import com.example.movieapps.viewmodel.MovieViewModel
import com.example.movieapps.viewmodel.NetworkResponse

@Composable
fun MovieListScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var movieList by remember { mutableStateOf(emptyList<MovieListModel>()) }

    val movieViewModel: MovieViewModel = hiltViewModel()
    val movieListResult = movieViewModel.movieListResult.observeAsState()

    val authViewModel: LoginAndSignUpViewModel = hiltViewModel()
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(movieList) {
        movieViewModel.getCacheDataOnFirstLaunch()
        if (movieListResult.value is NetworkResponse.Success) {
            movieList =
                (movieListResult.value as NetworkResponse.Success<List<MovieListModel>>).data
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { value ->
                    searchQuery = value
                },
                singleLine = true,
                placeholder = {
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = stringResource(id = R.string.search_bar_hints),
                        color = Purple40
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Adjusts the TextField width to leave space for the icon
                    .border(width = 1.dp, color = Purple40, shape = RoundedCornerShape(6.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        movieViewModel.performSearch(searchQuery.text.trim())
                    }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "search icon",
                            tint = Purple40
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    authViewModel.performSignout()
                    movieViewModel.deleteAllData()
                    if (authState.value == AuthState.Unauthenticated) {
                        navController.navigate(route = "onboarding_screen")
                    }
                },
            ) {
                Icon(
                    Icons.Default.ExitToApp,
                    contentDescription = "",
                    tint = Purple40
                )
            }
        }

        // network validation
        when (val result = movieListResult.value) {
            is NetworkResponse.Success -> {
                movieList = result.data
            }

            is NetworkResponse.Error -> {
                Text(
                    text = result.errorMessage,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally)
                )
            }

            NetworkResponse.Loading -> {
                movieList = emptyList()
                Spacer(modifier = Modifier.height(64.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterHorizontally)
                )
            }

            null -> {}
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Movie Grid List
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(movieList) { movie ->
                MovieItem(movie = movie, onClick = {
                    navController.navigate(route = "movieDetail_screen/${movie.imdbID}")
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieItem(movie: MovieListModel, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = movie.poster),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = movie.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 3,
                    color = Color.White
                )
            }
        }
    }
}

// Example data class for a Movie
data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String
)

@Preview
@Composable
fun MovieListScreenPreview() {
    MovieListScreen(rememberNavController())
}