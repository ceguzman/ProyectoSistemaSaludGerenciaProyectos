export interface ITypeDiseases {
  id: number;
  diseasesType?: string | null;
}

export type NewTypeDiseases = Omit<ITypeDiseases, 'id'> & { id: null };
