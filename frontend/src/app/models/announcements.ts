import { User } from "./user";

export interface Announcement {
  id: number,
  title: String,
  description: String,
  user: User,
  date: Date,
  address: String,
  participantsCap: number
}