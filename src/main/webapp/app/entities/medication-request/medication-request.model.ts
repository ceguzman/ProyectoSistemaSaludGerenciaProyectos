import { IMedicalAuthorization } from 'app/entities/medical-authorization/medical-authorization.model';

export interface IMedicationRequest {
  id: number;
  name?: string | null;
  amount?: number | null;
  milligrams?: number | null;
  medicalAuthorization?: Pick<IMedicalAuthorization, 'id' | 'detailAuthorization'> | null;
}

export type NewMedicationRequest = Omit<IMedicationRequest, 'id'> & { id: null };
