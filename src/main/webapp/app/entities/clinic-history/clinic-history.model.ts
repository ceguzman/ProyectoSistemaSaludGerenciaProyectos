import dayjs from 'dayjs/esm';
import { ITypeDiseases } from 'app/entities/type-diseases/type-diseases.model';
import { IPeople } from 'app/entities/people/people.model';

export interface IClinicHistory {
  id: number;
  dateClinic?: dayjs.Dayjs | null;
  typeDisease?: Pick<ITypeDiseases, 'id' | 'diseasesType'> | null;
  people?: Pick<IPeople, 'id' | 'documentNumber'> | null;
}

export type NewClinicHistory = Omit<IClinicHistory, 'id'> & { id: null };
