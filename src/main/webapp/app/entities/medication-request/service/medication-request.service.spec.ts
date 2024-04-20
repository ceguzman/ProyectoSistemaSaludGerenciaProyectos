import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMedicationRequest } from '../medication-request.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../medication-request.test-samples';

import { MedicationRequestService } from './medication-request.service';

const requireRestSample: IMedicationRequest = {
  ...sampleWithRequiredData,
};

describe('MedicationRequest Service', () => {
  let service: MedicationRequestService;
  let httpMock: HttpTestingController;
  let expectedResult: IMedicationRequest | IMedicationRequest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MedicationRequestService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a MedicationRequest', () => {
      const medicationRequest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(medicationRequest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MedicationRequest', () => {
      const medicationRequest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(medicationRequest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MedicationRequest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MedicationRequest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MedicationRequest', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMedicationRequestToCollectionIfMissing', () => {
      it('should add a MedicationRequest to an empty array', () => {
        const medicationRequest: IMedicationRequest = sampleWithRequiredData;
        expectedResult = service.addMedicationRequestToCollectionIfMissing([], medicationRequest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicationRequest);
      });

      it('should not add a MedicationRequest to an array that contains it', () => {
        const medicationRequest: IMedicationRequest = sampleWithRequiredData;
        const medicationRequestCollection: IMedicationRequest[] = [
          {
            ...medicationRequest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMedicationRequestToCollectionIfMissing(medicationRequestCollection, medicationRequest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MedicationRequest to an array that doesn't contain it", () => {
        const medicationRequest: IMedicationRequest = sampleWithRequiredData;
        const medicationRequestCollection: IMedicationRequest[] = [sampleWithPartialData];
        expectedResult = service.addMedicationRequestToCollectionIfMissing(medicationRequestCollection, medicationRequest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicationRequest);
      });

      it('should add only unique MedicationRequest to an array', () => {
        const medicationRequestArray: IMedicationRequest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const medicationRequestCollection: IMedicationRequest[] = [sampleWithRequiredData];
        expectedResult = service.addMedicationRequestToCollectionIfMissing(medicationRequestCollection, ...medicationRequestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const medicationRequest: IMedicationRequest = sampleWithRequiredData;
        const medicationRequest2: IMedicationRequest = sampleWithPartialData;
        expectedResult = service.addMedicationRequestToCollectionIfMissing([], medicationRequest, medicationRequest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicationRequest);
        expect(expectedResult).toContain(medicationRequest2);
      });

      it('should accept null and undefined values', () => {
        const medicationRequest: IMedicationRequest = sampleWithRequiredData;
        expectedResult = service.addMedicationRequestToCollectionIfMissing([], null, medicationRequest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicationRequest);
      });

      it('should return initial array if no MedicationRequest is added', () => {
        const medicationRequestCollection: IMedicationRequest[] = [sampleWithRequiredData];
        expectedResult = service.addMedicationRequestToCollectionIfMissing(medicationRequestCollection, undefined, null);
        expect(expectedResult).toEqual(medicationRequestCollection);
      });
    });

    describe('compareMedicationRequest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMedicationRequest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMedicationRequest(entity1, entity2);
        const compareResult2 = service.compareMedicationRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMedicationRequest(entity1, entity2);
        const compareResult2 = service.compareMedicationRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMedicationRequest(entity1, entity2);
        const compareResult2 = service.compareMedicationRequest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
