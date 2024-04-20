import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ITypeDiseases } from 'app/entities/type-diseases/type-diseases.model';
import { TypeDiseasesService } from 'app/entities/type-diseases/service/type-diseases.service';
import { IPeople } from 'app/entities/people/people.model';
import { PeopleService } from 'app/entities/people/service/people.service';
import { IClinicHistory } from '../clinic-history.model';
import { ClinicHistoryService } from '../service/clinic-history.service';
import { ClinicHistoryFormService } from './clinic-history-form.service';

import { ClinicHistoryUpdateComponent } from './clinic-history-update.component';

describe('ClinicHistory Management Update Component', () => {
  let comp: ClinicHistoryUpdateComponent;
  let fixture: ComponentFixture<ClinicHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let clinicHistoryFormService: ClinicHistoryFormService;
  let clinicHistoryService: ClinicHistoryService;
  let typeDiseasesService: TypeDiseasesService;
  let peopleService: PeopleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ClinicHistoryUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ClinicHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClinicHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clinicHistoryFormService = TestBed.inject(ClinicHistoryFormService);
    clinicHistoryService = TestBed.inject(ClinicHistoryService);
    typeDiseasesService = TestBed.inject(TypeDiseasesService);
    peopleService = TestBed.inject(PeopleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TypeDiseases query and add missing value', () => {
      const clinicHistory: IClinicHistory = { id: 456 };
      const typeDisease: ITypeDiseases = { id: 19691 };
      clinicHistory.typeDisease = typeDisease;

      const typeDiseasesCollection: ITypeDiseases[] = [{ id: 3262 }];
      jest.spyOn(typeDiseasesService, 'query').mockReturnValue(of(new HttpResponse({ body: typeDiseasesCollection })));
      const additionalTypeDiseases = [typeDisease];
      const expectedCollection: ITypeDiseases[] = [...additionalTypeDiseases, ...typeDiseasesCollection];
      jest.spyOn(typeDiseasesService, 'addTypeDiseasesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ clinicHistory });
      comp.ngOnInit();

      expect(typeDiseasesService.query).toHaveBeenCalled();
      expect(typeDiseasesService.addTypeDiseasesToCollectionIfMissing).toHaveBeenCalledWith(
        typeDiseasesCollection,
        ...additionalTypeDiseases.map(expect.objectContaining),
      );
      expect(comp.typeDiseasesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call People query and add missing value', () => {
      const clinicHistory: IClinicHistory = { id: 456 };
      const people: IPeople = { id: 7082 };
      clinicHistory.people = people;

      const peopleCollection: IPeople[] = [{ id: 24157 }];
      jest.spyOn(peopleService, 'query').mockReturnValue(of(new HttpResponse({ body: peopleCollection })));
      const additionalPeople = [people];
      const expectedCollection: IPeople[] = [...additionalPeople, ...peopleCollection];
      jest.spyOn(peopleService, 'addPeopleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ clinicHistory });
      comp.ngOnInit();

      expect(peopleService.query).toHaveBeenCalled();
      expect(peopleService.addPeopleToCollectionIfMissing).toHaveBeenCalledWith(
        peopleCollection,
        ...additionalPeople.map(expect.objectContaining),
      );
      expect(comp.peopleSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const clinicHistory: IClinicHistory = { id: 456 };
      const typeDisease: ITypeDiseases = { id: 13259 };
      clinicHistory.typeDisease = typeDisease;
      const people: IPeople = { id: 30679 };
      clinicHistory.people = people;

      activatedRoute.data = of({ clinicHistory });
      comp.ngOnInit();

      expect(comp.typeDiseasesSharedCollection).toContain(typeDisease);
      expect(comp.peopleSharedCollection).toContain(people);
      expect(comp.clinicHistory).toEqual(clinicHistory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClinicHistory>>();
      const clinicHistory = { id: 123 };
      jest.spyOn(clinicHistoryFormService, 'getClinicHistory').mockReturnValue(clinicHistory);
      jest.spyOn(clinicHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clinicHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: clinicHistory }));
      saveSubject.complete();

      // THEN
      expect(clinicHistoryFormService.getClinicHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(clinicHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(clinicHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClinicHistory>>();
      const clinicHistory = { id: 123 };
      jest.spyOn(clinicHistoryFormService, 'getClinicHistory').mockReturnValue({ id: null });
      jest.spyOn(clinicHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clinicHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: clinicHistory }));
      saveSubject.complete();

      // THEN
      expect(clinicHistoryFormService.getClinicHistory).toHaveBeenCalled();
      expect(clinicHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClinicHistory>>();
      const clinicHistory = { id: 123 };
      jest.spyOn(clinicHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clinicHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(clinicHistoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypeDiseases', () => {
      it('Should forward to typeDiseasesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeDiseasesService, 'compareTypeDiseases');
        comp.compareTypeDiseases(entity, entity2);
        expect(typeDiseasesService.compareTypeDiseases).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePeople', () => {
      it('Should forward to peopleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(peopleService, 'comparePeople');
        comp.comparePeople(entity, entity2);
        expect(peopleService.comparePeople).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
