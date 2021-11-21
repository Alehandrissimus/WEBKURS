import { RegistrationRequest } from "./auth/register";

export interface User extends RegistrationRequest {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  description: string;
}