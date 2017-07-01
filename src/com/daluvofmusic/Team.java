package com.daluvofmusic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


/**
 * The team class provides management for players.
 */

public class Team {
    private static HashMap<UUID, Team> membersTeam = new HashMap<>(); //Tracks which team a member is in
    private String teamName;
    private ArrayList<UUID> members = new ArrayList<>(); //List of members for a team instance

    private Team() {
        this.teamName = "*_*";
    } //Dummy team instance, use teamName ("*_*") to detect; see: getMemberTeam

    public Team(String teamName) {
        this.teamName = teamName;
    } //Creates a team instance with no pre-existing members

    private static boolean memberHasTeam(Player player) {
        return membersTeam.containsKey(player.getUniqueId());
    } //Returns true if player is a member of any arena

    public static Team getMemberTeam(Player player) {
        Team temp = new Team();
        if(memberHasTeam(player)) {
            return membersTeam.get(player.getUniqueId());
        }
        else {
            return temp;
        }
    } //Returns a team object that the player passed is a member of; returns dummy team if player has no team

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    } //Who knows why you'd want to do this

    public String getTeamName() {
        return teamName;
    }

    public ArrayList getMembers() {
        return members;
    }

    public boolean addMember(Player player){
        if(!memberHasTeam(player)) {
            membersTeam.put(player.getUniqueId(), this);
            members.add(player.getUniqueId());
            return true;
        }
        return false; //Cannot add a player to more than one team
    } //Returns true if successful; player can only be a member of one team at a time

    public boolean removeMember(Player player) {
        if(this.teamName==getMemberTeam(player).getTeamName()) {
            membersTeam.remove(player.getUniqueId());
            members.remove(player.getUniqueId());
            return true;
        }
        return false;
    } //Returns true if successful; false means player is not a member of this team instance

    public void removeAllMembers() {
        for(UUID uuid: members) {
            removeMember(Bukkit.getPlayer(uuid));
        }
    } //Removes all members of this team instance
}
