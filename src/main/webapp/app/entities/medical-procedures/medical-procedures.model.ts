import dayjs from 'dayjs/esm';
import { IMedicalAuthorization } from 'app/entities/medical-authorization/medical-authorization.model';

export interface IMedicalProcedures {
  id: number;
  typeProcedures?: string | null;
  description?: string | null;
  dateProcedures?: dayjs.Dayjs | null;
  medicalAuthorization?: Pick<IMedicalAuthorization, 'id' | 'detailAuthorization'> | null;
}

export type NewMedicalProcedures = Omit<IMedicalProcedures, 'id'> & { id: null };
