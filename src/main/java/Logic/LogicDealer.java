package Logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

public class LogicDealer {

    private static final Stone[][][] board = new Stone[3][3][3];
    private static final ArrayList<Stone> playerOneStones = new ArrayList<>();
    private static final ArrayList<Stone> playerTwoStones = new ArrayList<>();
    private static ArrayList<Stone[]> player_one_mill = new ArrayList<>();
    private static ArrayList<Stone[]> player_two_mill = new ArrayList<>();
    private String playerOne;
    private String playerTwo;

    public LogicDealer() {
    }

    public LogicDealer(String playerOne, String playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    /**
     * puts stones on board and in the player_stones Array
     *
     * @param stone
     */
    public void placeStone(Stone stone) {
        board[stone.getPosOne()][stone.getPosTwo()][stone.getPosThree()] = stone;
        if (stone.getPlayer().equalsIgnoreCase(this.playerOne)) {
            playerOneStones.add(stone);
        } else if (stone.getPlayer().equalsIgnoreCase(this.playerTwo)) {
            playerTwoStones.add(stone);
        } else {
            System.err.println("[Logic] false Player name" + stone.getPlayer());
        }
    }

    /**
     * Tests if there is a mill / mills for one player.
     * Since there are only 3 different positions, the positions are mostly hardcoded.
     *
     * @param player current player
     */
    public boolean muehle(String player) {
        ArrayList<Stone> temp_player_stones = null;
        ArrayList<Stone[]> temp_player_mill = new ArrayList<>();
        int player_mill_size = 0;
        if (Objects.equals(player, playerOne)) {
            temp_player_stones = playerOneStones;
            player_mill_size = player_one_mill.size();
            player_one_mill.clear();
        } else if (Objects.equals(player, playerTwo)) {
            temp_player_stones = playerTwoStones;
            player_mill_size = player_two_mill.size();
            player_two_mill.clear();
        } else {
            System.err.println("[LOGIC] Wrong Player!");
        }
        assert temp_player_stones != null;
        for (Stone s : temp_player_stones) {
            if (s.getPosTwo() == 0 && s.getPosThree() == 0) {
                for (Stone st : temp_player_stones) {
                    if (st.getPosThree() == 1 && st.getPosTwo() == 0) {
                        for (Stone stone : temp_player_stones) {
                            if (stone.getPosThree() == 2 && stone.getPosTwo() == 0) {
                                if ((s.getPosOne() == st.getPosOne()) && (s.getPosOne() == stone.getPosOne())) {
                                    temp_player_mill.add(new Stone[]{s, st, stone});
                                }
                            }
                        }
                    } else if (st.getPosTwo() == 1 && st.getPosThree() == 0) {
                        for (Stone stone : temp_player_stones) {
                            if (stone.getPosTwo() == 2 && stone.getPosThree() == 0) {
                                if ((s.getPosOne() == st.getPosOne()) && (s.getPosOne() == stone.getPosOne())) {
                                    temp_player_mill.add(new Stone[]{s, st, stone});
                                }
                            }
                        }
                    }
                }
            }
            if (s.getPosTwo() == 0 || s.getPosThree() == 0) {
                for (Stone st : temp_player_stones) {
                    if (s.getPosTwo() == 0) {
                        if (st.getPosTwo() == 1) {
                            for (Stone stone : temp_player_stones) {
                                if (stone.getPosTwo() == 2) {
                                    if ((s.getPosOne() == st.getPosOne()) && (s.getPosOne() == stone.getPosOne())
                                            && ((s.getPosThree() == st.getPosThree()) && (s.getPosThree() == stone.getPosThree()))) {
                                        temp_player_mill.add(new Stone[]{s, st, stone});
                                    }
                                }
                            }
                        }
                    } else if (s.getPosThree() == 0) {
                        if (st.getPosThree() == 1) {
                            for (Stone stone : temp_player_stones) {
                                if (stone.getPosThree() == 2) {
                                    if ((s.getPosOne() == st.getPosOne()) && (s.getPosOne() == stone.getPosOne())
                                            && ((s.getPosTwo() == st.getPosTwo()) && (s.getPosTwo() == stone.getPosTwo()))) {
                                        temp_player_mill.add(new Stone[]{s, st, stone});
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (s.getPosOne() == 0 && (s.getPosTwo() == 1 || s.getPosThree() == 1)) {
                for (Stone st : temp_player_stones) {
                    if (s.getPosThree() == 1) {
                        if (st.getPosOne() == 1 && (s.getPosTwo() == st.getPosTwo())) {
                            for (Stone stone : temp_player_stones) {
                                if (stone.getPosOne() == 2 && (s.getPosTwo() == stone.getPosTwo())) {
                                    temp_player_mill.add(new Stone[]{s, st, stone});
                                }
                            }
                        }
                    } else {
                        if (s.getPosTwo() == 1) {
                            if (st.getPosOne() == 1 && (s.getPosThree() == st.getPosThree())) {
                                for (Stone stone : temp_player_stones) {
                                    if (stone.getPosOne() == 2 && (s.getPosThree() == stone.getPosThree())) {
                                        temp_player_mill.add(new Stone[]{s, st, stone});
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (Objects.equals(player, playerOne)) {
            player_one_mill = temp_player_mill;
            return player_one_mill.size() > player_mill_size;
        } else if (Objects.equals(player, playerTwo)) {
            player_two_mill = temp_player_mill;
            return player_two_mill.size() > player_mill_size;
        }
        return false;
    }

    /**
     * Checks for an edge case
     * if there are two mills around a corner it only counts as one.
     *
     * @param mills      current mills
     * @param new_stones checks if the new stones are already used in a mill
     * @return
     */
    private boolean check_double_mill(ArrayList<Stone[]> mills, Stone[] new_stones) {
        for (Stone[] stones : mills) {
            for (Stone stone : stones) {
                for (Stone stone1 : new_stones) {
                    if (stone.equal(stone1)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Checks if a stone coherent to a player
     *
     * @param stone
     * @param player
     * @return true -> if the stone corosponse to a player
     */
    public boolean stone_player(Stone stone, String player) {
        if (Objects.equals(player, playerOne)) {
            for (Stone s : playerOneStones) {
                if (s.equal(stone)) {
                    return true;
                }
            }
        } else {
            for (Stone s : playerTwoStones) {
                if (s.equal(stone)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if a stone is in a mill
     *
     * @param stone
     * @param player
     * @return false -> is in a mill | true -> isn't in a mill
     */
    private boolean check(Stone stone, String player) {
        if (Objects.equals(player, playerOne)) {
            for (Stone[] stones : player_one_mill) {
                for (Stone s : stones) {
                    if (s.equal(stone)) {
                        return false;
                    }
                }
            }
        } else if (Objects.equals(player, playerTwo)) {
            for (Stone[] stones : player_two_mill) {
                for (Stone s : stones) {
                    if (s.equal(stone)) {
                        return false;
                    }
                }
            }
        } else {
            System.err.println("[LOGIC] Wrong Player!");
        }
        return true;
    }

    /**
     * Checks if one player only got mills
     *
     * @param player
     * @return true -> there are only mills
     */
    private boolean onlymills(String player) {
        if (Objects.equals(playerOne, player)) {
            for (Stone[] stones : player_one_mill) {
                for (Stone stone : playerOneStones) {
                    if (Arrays.asList(stones).contains(stone)) {

                    }
                }
            }
        } else if (Objects.equals(playerTwo, player)) {
            for (Stone[] stones : player_two_mill) {
                for (Stone temp : stones) {
                    for (Stone stone : playerTwoStones) {
                        if (!stone.equal(temp)) {
                            System.out.println("here2: " + temp);
                            System.out.println("under heare: " + stone);
                            return false;
                        }
                    }
                }
            }
        } else {
            System.err.println("[LOGIC] Wrong Player!");
        }
        return true;
    }

    public boolean remove(Stone stone, String player) {
        System.out.println(stone);
        System.out.println(player);
        if (stone_player(stone, player)) {
            System.out.println(onlymills(player));
            print_mills();
            if ((!check(stone, player) && onlymills(player)) || check(stone, player)) {
                board[stone.getPosOne()][stone.getPosTwo()][stone.getPosThree()] = null;
                if (Objects.equals(player, playerOne)) {
                    for (Stone pl_one : playerOneStones) {
                        if (pl_one.equal(stone)) {
                            playerOneStones.remove(pl_one);
                            return true;
                        }
                    }
                } else if (Objects.equals(player, playerTwo)) {
                    for (Stone pl_two : playerTwoStones) {
                        if (pl_two.equal(stone)) {
                            playerTwoStones.remove(pl_two);
                            return true;
                        }
                    }
                } else {
                }
            }
        }
        System.out.println("[LOGIC] Wrong Stone");
        return false;
    }

    public boolean move_possible(Stone start, Stone destination, String player) {
        ArrayList<Stone> temp = null;
        boolean move_is_possible = false;
        if (board[destination.getPosOne()][destination.getPosTwo()][destination.getPosThree()] == null) {
            if (player.equals(playerOne)) {
                temp = playerOneStones;
            } else if (player.equals(playerTwo)) {
                temp = playerTwoStones;
            } else {
                System.err.println("[LOGIC] Wrong Player! i move_possible");
            }
            if (start.getPosOne() == destination.getPosOne()) {
                if ((((start.getPosTwo() + 1) == destination.getPosTwo()) || ((start.getPosTwo() - 1) == destination.getPosTwo())) && start.getPosThree() == destination.getPosThree()) {
                    move_is_possible = true;
                } else if ((((start.getPosThree() + 1) == destination.getPosThree()) || ((start.getPosThree() - 1) == destination.getPosThree())) && start.getPosTwo() == destination.getPosTwo()) {
                    move_is_possible = true;
                }
            } else if ((start.getPosTwo() == 1 && destination.getPosTwo() == 1) && (((start.getPosOne() + 1) == destination.getPosOne()) || ((start.getPosOne() - 1) == destination.getPosOne()))) {
                if (start.getPosThree() == 0 && destination.getPosThree() == 0) {
                    move_is_possible = true;
                } else if (start.getPosThree() == 2 && destination.getPosThree() == 2) {
                    move_is_possible = true;
                }
            } else if (start.getPosThree() == 1 && destination.getPosThree() == 1 && (((start.getPosOne() + 1) == destination.getPosOne()) || ((start.getPosOne() - 1) == destination.getPosOne()))) {
                if (start.getPosTwo() == 0 && destination.getPosTwo() == 0) {
                    move_is_possible = true;
                } else if (start.getPosTwo() == 2 && destination.getPosTwo() == 2) {
                    move_is_possible = true;
                }
            }
            if (move_is_possible) {
                assert temp != null;
                for (Stone stone : temp) {
                    if (stone.equal(start)) {
                        temp.remove(stone);
                        temp.add(destination);
                    }
                }
            }
        }

        return move_is_possible;
    }


    private void print_mills() {
        int x = 0;
        System.out.println(playerOne + "'s mills:");
        for (Stone[] stones : player_one_mill) {
            System.out.println("MILL " + x);
            for (Stone stone : stones) {
                System.out.println(stone);
            }
            x++;
        }
        System.out.println();
        System.out.println(playerTwo + "'s mills:");
        for (Stone[] stones : player_two_mill) {
            for (Stone stone : stones) {
                System.out.println(stone);
            }
        }
    }
}