import { ITypeDocument } from 'app/entities/type-document/type-document.model';
import { ITypeSpecialist } from 'app/entities/type-specialist/type-specialist.model';

export interface IPeople {
  id: number;
  documentNumber?: string | null;
  firstName?: string | null;
  firstSurname?: string | null;
  secondName?: string | null;
  secondSurname?: string | null;
  typeDocument?: Pick<ITypeDocument, 'id' | 'documentName'> | null;
  typeSpecialist?: Pick<ITypeSpecialist, 'id' | 'specialistType'> | null;
}

export type NewPeople = Omit<IPeople, 'id'> & { id: null };
