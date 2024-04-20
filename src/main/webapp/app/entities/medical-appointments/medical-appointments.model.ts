import dayjs from 'dayjs/esm';
import { ITypeSpecialist } from 'app/entities/type-specialist/type-specialist.model';

export interface IMedicalAppointments {
  id: number;
  dateMedical?: dayjs.Dayjs | null;
  typeSpecialist?: Pick<ITypeSpecialist, 'id' | 'specialistType'> | null;
}

export type NewMedicalAppointments = Omit<IMedicalAppointments, 'id'> & { id: null };
