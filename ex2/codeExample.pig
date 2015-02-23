-- Load data
movies = LOAD '/home/BIGdata/bdc-ws14/opt/data/movies_data.csv' USING PigStorage(',') as (id,name,year,rating,duration);

-- List the movies that having a rating greater than 4
movies_greater_than_four = FILTER movies BY (float)rating>4.0;
DUMP movies_greater_than_four;

-- Save results
STORE movies_greater_than_four into 'movies_data_four';

-- Specify type → no cast operation needed in FILTER
movies = LOAD '/home/BIGdata/bdc-ws14/opt/data/movies_data.csv' USING PigStorage(',') as (id:int,name:chararray,year:int,rating:double,duration:int);

-- List the movies that were released between 1950 and 1960
movies_between_50_60 = FILTER movies by year>1950 and year<1960;

-- List the movies that start with the A 
movies_starting_with_A = FILTER movies by name matches 'A.*';

-- List the movies that have duration greater that 2 hours
movies_duration_2_hrs = FILTER movies by duration > 7200;

-- List the movies that have rating between 3 and 4
movies_rating_3_4 = FILTER movies BY rating>3.0 and rating<4.0; 

-- The schema of a relation/alias can be viewed using the DESCRIBE command:
DESCRIBE movies;

-- List the movie names its duration in minutes
movie_duration = FOREACH movies GENERATE name, (double)(duration/60);

-- List the years and the number of movies released each year.
grouped_by_year = group movies by year;
count_by_year = FOREACH grouped_by_year GENERATE group, COUNT(movies);

-- List all the movies in the ascending order of year.
asc_movies_by_year = ORDER movies BY year ASC;

-- List all the movies in the descending order of year.
desc_movies_by_year = ORDER movies by year DESC;

-- Use the sample keyword to get sample set from your data.
sample_10_percent = sample movies 0.1;
