import dayjs from 'dayjs/esm';
import { IClinicHistory } from 'app/entities/clinic-history/clinic-history.model';
import { State } from 'app/entities/enumerations/state.model';

export interface IMedicalAuthorization {
  id: number;
  detailAuthorization?: string | null;
  stateAuthorization?: keyof typeof State | null;
  dateAuthorization?: dayjs.Dayjs | null;
  clinicHistory?: Pick<IClinicHistory, 'id' | 'dateClinic'> | null;
}

export type NewMedicalAuthorization = Omit<IMedicalAuthorization, 'id'> & { id: null };
