import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ITypeDocument } from 'app/entities/type-document/type-document.model';
import { TypeDocumentService } from 'app/entities/type-document/service/type-document.service';
import { ITypeSpecialist } from 'app/entities/type-specialist/type-specialist.model';
import { TypeSpecialistService } from 'app/entities/type-specialist/service/type-specialist.service';
import { IPeople } from '../people.model';
import { PeopleService } from '../service/people.service';
import { PeopleFormService } from './people-form.service';

import { PeopleUpdateComponent } from './people-update.component';

describe('People Management Update Component', () => {
  let comp: PeopleUpdateComponent;
  let fixture: ComponentFixture<PeopleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let peopleFormService: PeopleFormService;
  let peopleService: PeopleService;
  let typeDocumentService: TypeDocumentService;
  let typeSpecialistService: TypeSpecialistService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, PeopleUpdateComponent],
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
      .overrideTemplate(PeopleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PeopleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    peopleFormService = TestBed.inject(PeopleFormService);
    peopleService = TestBed.inject(PeopleService);
    typeDocumentService = TestBed.inject(TypeDocumentService);
    typeSpecialistService = TestBed.inject(TypeSpecialistService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TypeDocument query and add missing value', () => {
      const people: IPeople = { id: 456 };
      const typeDocument: ITypeDocument = { id: 20854 };
      people.typeDocument = typeDocument;

      const typeDocumentCollection: ITypeDocument[] = [{ id: 19584 }];
      jest.spyOn(typeDocumentService, 'query').mockReturnValue(of(new HttpResponse({ body: typeDocumentCollection })));
      const additionalTypeDocuments = [typeDocument];
      const expectedCollection: ITypeDocument[] = [...additionalTypeDocuments, ...typeDocumentCollection];
      jest.spyOn(typeDocumentService, 'addTypeDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ people });
      comp.ngOnInit();

      expect(typeDocumentService.query).toHaveBeenCalled();
      expect(typeDocumentService.addTypeDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        typeDocumentCollection,
        ...additionalTypeDocuments.map(expect.objectContaining),
      );
      expect(comp.typeDocumentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TypeSpecialist query and add missing value', () => {
      const people: IPeople = { id: 456 };
      const typeSpecialist: ITypeSpecialist = { id: 30967 };
      people.typeSpecialist = typeSpecialist;

      const typeSpecialistCollection: ITypeSpecialist[] = [{ id: 19261 }];
      jest.spyOn(typeSpecialistService, 'query').mockReturnValue(of(new HttpResponse({ body: typeSpecialistCollection })));
      const additionalTypeSpecialists = [typeSpecialist];
      const expectedCollection: ITypeSpecialist[] = [...additionalTypeSpecialists, ...typeSpecialistCollection];
      jest.spyOn(typeSpecialistService, 'addTypeSpecialistToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ people });
      comp.ngOnInit();

      expect(typeSpecialistService.query).toHaveBeenCalled();
      expect(typeSpecialistService.addTypeSpecialistToCollectionIfMissing).toHaveBeenCalledWith(
        typeSpecialistCollection,
        ...additionalTypeSpecialists.map(expect.objectContaining),
      );
      expect(comp.typeSpecialistsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const people: IPeople = { id: 456 };
      const typeDocument: ITypeDocument = { id: 21321 };
      people.typeDocument = typeDocument;
      const typeSpecialist: ITypeSpecialist = { id: 23953 };
      people.typeSpecialist = typeSpecialist;

      activatedRoute.data = of({ people });
      comp.ngOnInit();

      expect(comp.typeDocumentsSharedCollection).toContain(typeDocument);
      expect(comp.typeSpecialistsSharedCollection).toContain(typeSpecialist);
      expect(comp.people).toEqual(people);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPeople>>();
      const people = { id: 123 };
      jest.spyOn(peopleFormService, 'getPeople').mockReturnValue(people);
      jest.spyOn(peopleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ people });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: people }));
      saveSubject.complete();

      // THEN
      expect(peopleFormService.getPeople).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(peopleService.update).toHaveBeenCalledWith(expect.objectContaining(people));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPeople>>();
      const people = { id: 123 };
      jest.spyOn(peopleFormService, 'getPeople').mockReturnValue({ id: null });
      jest.spyOn(peopleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ people: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: people }));
      saveSubject.complete();

      // THEN
      expect(peopleFormService.getPeople).toHaveBeenCalled();
      expect(peopleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPeople>>();
      const people = { id: 123 };
      jest.spyOn(peopleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ people });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(peopleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypeDocument', () => {
      it('Should forward to typeDocumentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeDocumentService, 'compareTypeDocument');
        comp.compareTypeDocument(entity, entity2);
        expect(typeDocumentService.compareTypeDocument).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTypeSpecialist', () => {
      it('Should forward to typeSpecialistService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeSpecialistService, 'compareTypeSpecialist');
        comp.compareTypeSpecialist(entity, entity2);
        expect(typeSpecialistService.compareTypeSpecialist).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
