use regex::Regex;
use std::collections::HashMap;
use std::fs;

struct Game {
    id: i32,
    extractions: Vec<HashMap<String, i32>>,
}

impl Game {
    fn new(id: i32) -> Game {
        Game {
            id,
            extractions: vec![],
        }
    }
}

fn main() {
    let input = fs::read_to_string("input.txt").unwrap();

    let mut games: Vec<Game> = vec![];
    let patt = Regex::new(r"((\d+) (red|green|blue),?)+").unwrap();

    for line in input.lines() {
        let start_i = line.find(":").unwrap() + 2;
        let game_id = i32::from_str_radix(
            Regex::new(r"Game (\d+):")
                .unwrap()
                .captures(line)
                .unwrap()
                .get(1)
                .unwrap()
                .as_str(),
            10,
        )
        .unwrap();

        let mut game = Game::new(game_id);

        let extr_strs = line[start_i..].split(";");

        for extraction in extr_strs {
            let mut extr = HashMap::new();
            extr.insert("red".to_string(), 0);
            extr.insert("green".to_string(), 0);
            extr.insert("blue".to_string(), 0);

            for pattern in patt.captures_iter(extraction) {
                extr.insert(
                    pattern.get(3).unwrap().as_str().to_string(),
                    i32::from_str_radix(pattern.get(2).unwrap().as_str(), 10).unwrap(),
                );
            }

            game.extractions.push(extr);
        }

        games.push(game);
    }

    let mut sum_ids = 0;
    let mut sum_powers = 0;

    for game in games.iter() {
        let mut reds = 0;
        let mut greens = 0;
        let mut blues = 0;

        for extraction in game.extractions.iter() {
            reds = std::cmp::max(reds, extraction["red"]);
            greens = std::cmp::max(greens, extraction["green"]);
            blues = std::cmp::max(blues, extraction["blue"]);
        }

        if reds <= 12 && greens <= 13 && blues <= 14 {
            sum_ids += game.id;
        }

        let power = reds * greens * blues;
        sum_powers += power;
    }

    println!("SUM OF IDS IS {}", sum_ids);
    println!("SUM OF POWERS IS {}", sum_powers);
}
