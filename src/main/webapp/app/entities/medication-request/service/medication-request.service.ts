import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedicationRequest, NewMedicationRequest } from '../medication-request.model';

export type PartialUpdateMedicationRequest = Partial<IMedicationRequest> & Pick<IMedicationRequest, 'id'>;

export type EntityResponseType = HttpResponse<IMedicationRequest>;
export type EntityArrayResponseType = HttpResponse<IMedicationRequest[]>;

@Injectable({ providedIn: 'root' })
export class MedicationRequestService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/medication-requests');

  create(medicationRequest: NewMedicationRequest): Observable<EntityResponseType> {
    return this.http.post<IMedicationRequest>(this.resourceUrl, medicationRequest, { observe: 'response' });
  }

  update(medicationRequest: IMedicationRequest): Observable<EntityResponseType> {
    return this.http.put<IMedicationRequest>(
      `${this.resourceUrl}/${this.getMedicationRequestIdentifier(medicationRequest)}`,
      medicationRequest,
      { observe: 'response' },
    );
  }

  partialUpdate(medicationRequest: PartialUpdateMedicationRequest): Observable<EntityResponseType> {
    return this.http.patch<IMedicationRequest>(
      `${this.resourceUrl}/${this.getMedicationRequestIdentifier(medicationRequest)}`,
      medicationRequest,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMedicationRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMedicationRequest[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMedicationRequestIdentifier(medicationRequest: Pick<IMedicationRequest, 'id'>): number {
    return medicationRequest.id;
  }

  compareMedicationRequest(o1: Pick<IMedicationRequest, 'id'> | null, o2: Pick<IMedicationRequest, 'id'> | null): boolean {
    return o1 && o2 ? this.getMedicationRequestIdentifier(o1) === this.getMedicationRequestIdentifier(o2) : o1 === o2;
  }

  addMedicationRequestToCollectionIfMissing<Type extends Pick<IMedicationRequest, 'id'>>(
    medicationRequestCollection: Type[],
    ...medicationRequestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const medicationRequests: Type[] = medicationRequestsToCheck.filter(isPresent);
    if (medicationRequests.length > 0) {
      const medicationRequestCollectionIdentifiers = medicationRequestCollection.map(medicationRequestItem =>
        this.getMedicationRequestIdentifier(medicationRequestItem),
      );
      const medicationRequestsToAdd = medicationRequests.filter(medicationRequestItem => {
        const medicationRequestIdentifier = this.getMedicationRequestIdentifier(medicationRequestItem);
        if (medicationRequestCollectionIdentifiers.includes(medicationRequestIdentifier)) {
          return false;
        }
        medicationRequestCollectionIdentifiers.push(medicationRequestIdentifier);
        return true;
      });
      return [...medicationRequestsToAdd, ...medicationRequestCollection];
    }
    return medicationRequestCollection;
  }
}
