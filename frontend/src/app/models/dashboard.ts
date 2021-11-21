import { User } from "./user";

export interface Dashboard {
  id: number,
  title: String,
  description: String,
  owner: User,
  date: Date,
  address: String,
  likes?: number
}