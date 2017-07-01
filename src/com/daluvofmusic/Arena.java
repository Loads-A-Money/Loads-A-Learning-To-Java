package com.daluvofmusic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.daluvofmusic.Team.getMemberTeam;


/**
 * The arena class provides management for members and teams.
 * Also keeps track of all created arenas, keyed by id.
 */

public class Arena {
    //TODO data storage
    private static HashMap<Integer, Arena> arenas = new HashMap<>(); //Stores all created arenas
    private static HashMap<UUID, Arena> membersArena = new HashMap<>(); //Tracks which arena a member is in
    private final int id;
    private final ArrayList<Location> spawns;
    private ArrayList<Team> teams = new ArrayList<>(); //List of teams for an arena instance
    private ArrayList<UUID> members = new ArrayList<>(); //List of members for an arena instance

    private Arena() {
        this.id =  -2147483648;
        spawns = null;
    } //Dummy arena instance, use id (-2147483648) to detect; see: getArena & getMemberArena

    public Arena(int id, ArrayList<Location> spawns) {
        this.id = id;
        this.spawns = spawns;
        arenas.put(id, this);
    } //Creates an arena instance with no pre-existing teams

    public Arena(int id, ArrayList<Location> spawns, ArrayList<Team> teams) {
        this.id = id;
        this.spawns = spawns;
        this.teams = teams;
        arenas.put(id, this);
    } //Creates an arena instance, will have no pre-existing members

    private static boolean memberHasArena(Player player) {
        return membersArena.containsKey(player.getUniqueId());
    } //Returns true if player is a member of any arena

    private static boolean isArena(int id) {
        return arenas.containsKey(id);
    } //Returns true if an arena exists with id passed

    private boolean arenaHasTeam(Team team) {
        return teams.contains(team);
    } //Returns true if the team is in this arena instance

    public static Arena getArena(int id) {
        Arena temp = new Arena();
        if(isArena(id)) {
            return arenas.get(id);
        }
        return temp;
    } //Returns an arena object with the id passed; returns dummy arena instance if no arena exists with id passed

    public static Arena getMemberArena(Player player) {
        Arena temp = new Arena();
        if(memberHasArena(player)) {
            return membersArena.get(player.getUniqueId());
        }
        return temp;
    } //Returns an arena object that the player passed is a member of; returns dummy arena if player has no arena

    public static boolean removeArena(int id) {
        if(isArena(id)) {
            getArena(id).removeAllMembers();
            arenas.remove(id);
            return true;
        }
        return false;
    } //Returns true if successful

    public int getId() {
        return id;
    }

    public ArrayList<Location> getSpawns() {
        return spawns;
    }

    public ArrayList<UUID> getMembers() {
        return members;
    }

    public boolean addMember(Player player) {
        if(!memberHasArena(player)) {
            membersArena.put(player.getUniqueId(), this);
            members.add(player.getUniqueId());
            return true;
        }
        return false; //Cannot add a player to more than one arena
    } //Returns true if successful; player can only be a member of one arena at a time

    public boolean removeMember(Player player) {
        if(this.id==getMemberArena(player).getId()) {
            getMemberTeam(player).removeMember(player);
            membersArena.remove(player.getUniqueId());
            members.remove(player.getUniqueId());
            return true;
        }
        return false;
    } //Returns true if successful; false means player is not a member this arena instance

    public void removeAllMembers() {
        for(UUID uuid: members) {
            removeMember(Bukkit.getPlayer(uuid));
        }
    } //Removes all members of this arena instance

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public boolean addTeam (Team team) {
        if(!arenaHasTeam(team)&&(team.getMembers().size()==0)) {
            teams.add(team);
            return true;
        }
        return false;
    } //Returns true if successful; team can only be in arena once; team must be empty
    //TODO DO NOT ADD AN IDENTICAL TEAM OBJECT TO MORE THAN ONE ARENA

    public boolean removeTeam (Team team) {
        if(arenaHasTeam(team)) {
            team.removeAllMembers();
            teams.remove(team);
            return true;
        }
        return false;
    } //Returns true if successful; false means team was not in this arena instance

    public void removeAllTeams() {
        for(Team team: teams) {
            teams.remove(team);
        }
    } //Removes all teams of this arena instance
}