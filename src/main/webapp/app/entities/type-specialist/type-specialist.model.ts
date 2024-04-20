import { State } from 'app/entities/enumerations/state.model';

export interface ITypeSpecialist {
  id: number;
  specialistType?: string | null;
  stateSpecialist?: keyof typeof State | null;
}

export type NewTypeSpecialist = Omit<ITypeSpecialist, 'id'> & { id: null };
