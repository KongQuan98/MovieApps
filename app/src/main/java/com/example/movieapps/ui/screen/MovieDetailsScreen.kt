import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.movieapps.domain.datamodel.MovieDetailsResponse
import com.example.movieapps.ui.theme.Pink40
import com.example.movieapps.ui.theme.Purple40
import com.example.movieapps.ui.theme.PurpleGrey40
import com.example.movieapps.viewmodel.MovieViewModel
import com.example.movieapps.viewmodel.NetworkResponse

@Composable
fun MovieDetailsScreen(navController: NavController, movieId: String?) {

    var movieDetail by remember { mutableStateOf<MovieDetailsResponse?>(null) }

    val movieViewModel: MovieViewModel = hiltViewModel()
    val movieDetailResult = movieViewModel.getMovieDetailResult.observeAsState()

    LaunchedEffect(movieDetailResult.value) {
        if (movieId != null) {
            movieViewModel.performGetMovieDetails(movieId = movieId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // network validation
        when (val result = movieDetailResult.value) {
            is NetworkResponse.Success -> {
                movieDetail = result.data
                MovieDetailPage(navController, movieDetail!!)
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
                Spacer(modifier = Modifier.height(64.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterHorizontally)
                )
            }

            null -> {}
        }


    }
}

@Composable
fun MovieDetailPage(navController: NavController, movieDetailModel: MovieDetailsResponse) {
    // Poster Image with overlay rating
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = movieDetailModel.poster),
            contentDescription = "Movie Poster",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Color(0xAA000000)) // Change the color and transparency as needed
        )

        Image(
            painter = rememberAsyncImagePainter(model = movieDetailModel.poster),
            contentDescription = "Movie Poster",
            modifier = Modifier
                .height(200.dp)
                .align(Alignment.BottomStart)
                .padding(start = 16.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
        )

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .background(color = Color.White, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = "Close",
                tint = Purple40
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        // Rating
        if (movieDetailModel.imdbRating != "N/A" || movieDetailModel.imdbVotes != "N/A") {
            Text(
                text = "${movieDetailModel.imdbRating} / 10 ⭐️ ${movieDetailModel.imdbVotes} Ratings",
                color = Pink40,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Title
        Text(
            text = movieDetailModel.title,
            color = Purple40,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Genre
        Text(
            text = movieDetailModel.genre,
            color = PurpleGrey40,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Plot Summary
        Text(
            text = "Plot Summary",
            color = Purple40,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Summary
        Text(
            text = movieDetailModel.plot,
            color = PurpleGrey40,
            fontSize = 16.sp,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Other Ratings
        if (movieDetailModel.ratings.isNotEmpty()) {
            Text(
                text = "Other Ratings",
                color = Purple40,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movieDetailModel.ratings) { rating ->
                    RatingCard(platform = rating.source, rating = rating.value)
                }
            }
        }
    }
}

@Composable
fun RatingCard(platform: String, rating: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F1F1))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = platform,
                color = PurpleGrey40,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                softWrap = false
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = rating,
                color = Purple40,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
