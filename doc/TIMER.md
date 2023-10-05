
# Workflow of the Timer


## 1. Setup of an Event

1. Generate an Event
    - Create/Read/Update/Delete Events
2. Generate all Competitors
    - Create/Read/Update/Delete Competitor
3. First 2 Round (one for male, one for female)
    - Create/Read/Update/Delete Rounds
4. First UserRound
    - Add each competitor to the first round
5. Print the first start list
    - Print the start list of the first 2 rounds (one for male, one for female)

### Extended:
1. Let competitors register for the event
    - Create/Read/Update/Delete competitors
2. Import competitors from a file
    - Create/Read/Update/Delete competitors

## 2. Run one round

1. Select a round you want to start with (male or female)
2. Start the round
3. Enter the users score

Repeat 1 - 3 until for the other gender

## 3. After a round

1. Create 2 new rounds (male and female)
2. Print the start list of the new rounds

## 4. After the last round

1. Print the results of the event

### Extended:
1. Send the results via emails

